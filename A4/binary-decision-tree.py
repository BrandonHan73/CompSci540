# %%
from ucimlrepo import fetch_ucirepo 

from scipy import stats as st

import math
import numpy as np
  
# fetch dataset 
breast_cancer_wisconsin_original = fetch_ucirepo(id=15) 
  
# data (as pandas dataframes) 
y = breast_cancer_wisconsin_original.data.targets[~breast_cancer_wisconsin_original.data.features.isnull().any(axis=1)]
X = breast_cancer_wisconsin_original.data.features.dropna()

feature_list = np.append(np.array(['', 'Sample_code_number']), breast_cancer_wisconsin_original.data.features.keys())

feature_index = 6
tree_index = [7, 4, 5, 10, 2, 6]

feature = feature_list[feature_index]

feature_data = X[feature].tolist()
tree_data = [X[feature_list[t]].tolist() for t in tree_index]
labels = [i[0] for i in y.values.tolist()]

test_set = [
[1002945,5,4,4,5,7,10,3,2,1],
[1016277,6,8,8,1,3,4,3,7,1],
[1018099,1,1,1,1,2,10,3,1,1],
[1033078,2,1,1,1,2,1,1,1,5],
[1035283,1,1,1,1,1,1,3,1,1],
[1043999,1,1,1,1,2,3,3,1,1],
[1049815,4,1,1,1,2,1,3,1,1],
[1056784,3,1,1,1,2,1,2,1,1],
[1066373,3,2,1,1,1,1,2,1,1],
[1067444,2,1,1,1,2,1,2,1,1],
[1070935,3,1,1,1,1,1,2,1,1],
[1074610,2,1,1,2,2,1,3,1,1],
[1079304,2,1,1,1,2,1,2,1,1],
[1103722,1,1,1,1,2,1,2,1,2],
[1106095,4,1,1,3,2,1,3,1,1],
[1116192,1,1,1,1,2,1,2,1,1],
[1121732,1,1,1,1,2,1,3,2,1],
[1124651,1,3,3,2,2,1,7,2,1],
[1132347,1,1,4,1,2,1,2,1,1],
[1133136,3,1,1,1,2,3,3,1,1],
[1137156,2,2,2,1,1,1,7,1,1],
[1143978,5,2,1,1,2,1,3,1,1],
[1152331,4,1,1,1,2,1,3,1,1],
[1156272,1,1,1,1,2,1,3,1,1],
[1157734,4,1,1,1,2,1,3,1,1],
[1160476,2,1,1,1,2,1,3,1,1],
[1165297,2,1,1,2,2,1,1,1,1],
[1167471,4,1,2,1,2,1,3,1,1],
[1171795,1,3,1,2,2,2,5,3,2],
[1173347,1,1,1,1,2,5,1,1,1],
[1173514,1,1,1,1,4,3,1,1,1],
[1174057,1,1,2,2,2,1,3,1,1],
[1176406,1,1,1,1,2,1,2,1,1],
[1177512,1,1,1,1,10,1,1,1,1],
[1179818,2,1,1,1,2,1,3,1,1],
[1180831,3,1,1,1,3,1,2,1,1],
[1182404,4,1,1,1,2,1,2,1,1],
[1183240,4,1,2,1,2,1,2,1,1],
[1183911,2,1,1,1,2,1,1,1,1],
[1184241,2,1,1,1,2,1,2,1,1],
[1187457,3,1,1,3,8,1,5,8,1],
[1190394,4,1,1,1,2,3,1,1,1],
[1193091,1,2,2,1,2,1,2,1,1],
[1197080,4,1,1,1,2,1,3,2,1],
[1197440,1,1,1,2,1,3,1,1,7],
[1198641,3,1,1,1,2,1,3,1,1],
[1199731,3,1,1,1,2,1,1,1,1],
[1200772,1,1,1,1,2,1,2,1,1],
[1202125,4,1,1,1,2,1,3,1,1],
[1204242,1,1,1,1,2,1,1,1,1],
[1206089,2,1,1,1,1,1,3,1,1],
[1212232,5,1,1,1,2,1,2,1,1],
[1212422,3,1,1,1,2,1,3,1,1],
[1213375,8,4,4,5,4,7,7,8,2],
[1214092,1,1,1,1,2,1,1,1,1],
[1216947,1,1,1,1,2,1,3,1,1],
[1217264,1,1,1,1,2,1,3,1,1],
[1218860,1,1,1,1,1,1,3,1,1],
[1220330,1,1,1,1,2,1,3,1,1],
[1223426,1,1,1,1,2,1,3,1,1],
[1224329,1,1,1,2,2,1,3,1,1],
[1228311,1,1,1,1,1,1,3,1,1],
[1236043,3,3,2,1,3,1,3,6,1],
[1276091,3,1,1,3,1,1,3,1,1],
[128059,1,1,1,1,2,5,5,1,1],
[1287775,5,1,1,2,2,2,3,1,1],
[183913,1,2,2,1,2,1,1,1,1],
[1182404,3,1,1,1,2,1,1,1,1],
[1198641,3,1,1,1,2,1,3,1,1],
[1182404,5,1,4,1,2,1,3,2,1],
[411453,5,1,1,1,2,1,3,1,1],
[434518,3,1,1,1,2,1,2,1,1],
[456282,1,1,1,1,2,1,3,1,1],
[486662,2,1,1,2,2,1,3,1,1],
[560680,1,1,1,1,2,1,1,1,1],
[616240,5,3,4,3,4,5,4,7,1],
[636130,1,1,1,1,2,1,3,1,1],
[666090,1,1,1,1,2,1,3,1,1],
[673637,3,1,1,1,2,5,5,1,1],
[688033,1,1,1,1,2,1,1,1,1],
[704097,1,1,1,1,1,1,2,1,1],
[721482,4,4,4,4,6,5,7,3,1],
[740492,1,1,1,1,2,1,3,1,1],
[756136,1,1,1,1,2,1,2,1,1],
[770066,5,2,2,2,2,1,2,2,1],
[798429,1,1,1,1,2,1,3,1,1],
[810104,1,1,1,1,2,1,3,1,1],
[814911,1,1,1,1,2,1,1,1,1],
[830690,5,2,2,2,3,1,1,3,1],
[836433,5,1,1,3,2,1,1,1,1],
[846832,3,4,5,3,7,3,4,6,1],
[857774,4,1,1,1,3,1,2,2,1],
[888523,4,4,4,2,2,3,2,1,1],
[897172,2,1,1,1,2,1,2,1,1],
[428598,1,1,3,1,1,1,2,1,1],
[493452,1,1,3,1,2,1,1,1,1],
[521441,5,1,1,2,2,1,2,1,1],
[636437,1,1,1,1,2,1,1,1,1],
[654244,1,1,1,1,1,1,2,1,1],
[685977,5,3,4,1,4,1,3,1,1],
[1041801,5,3,3,3,2,3,4,4,1],
[1047630,7,4,6,4,6,1,4,3,1],
[1054590,7,3,2,10,5,10,5,4,4,],
[1065726,5,2,3,4,2,7,3,6,1],
[1080185,10,10,10,8,6,1,8,9,1],
[1091262,2,5,3,3,6,7,7,5,1],
[1100524,6,10,10,2,8,10,7,3,3],
[1103608,10,10,10,4,8,1,8,10,1],
[1106829,7,8,7,2,4,8,3,8,2],
[1108449,5,3,3,4,2,4,3,4,1],
[1110503,5,5,5,8,10,8,7,3,7],
[1111249,10,6,6,3,4,5,3,6,1],
[1113038,8,2,4,1,5,1,5,4,4],
[1113906,9,5,5,2,2,2,5,1,1],
[1116116,9,10,10,1,10,8,3,3,1],
[1116998,10,4,2,1,3,2,4,3,10],
[1120559,8,3,8,3,4,9,8,9,8],
[1125035,9,4,5,10,6,10,4,8,1],
[1147699,3,5,7,8,8,9,7,10,7],
[1148278,3,3,6,4,5,8,4,4,1],
[1165926,9,6,9,2,10,6,2,9,10],
[1166654,10,3,5,1,10,5,3,10,2],
[1168359,8,2,3,1,6,3,7,1,1],
[1169049,7,3,4,4,3,3,3,2,7],
[1170420,1,6,8,10,8,10,5,7,1],
[1171845,8,6,4,3,5,9,3,1,1],
[1173216,10,10,10,3,10,8,8,1,1],
[1174131,10,10,10,2,10,10,5,3,3],
[1175937,5,4,6,7,9,7,8,10,1],
[1177399,8,3,5,4,5,10,1,6,2],
[1183983,9,5,5,4,4,5,4,3,3],
[1187805,8,8,7,4,10,10,7,8,7],
[1189286,10,10,8,6,4,5,8,10,1],
[1196295,9,9,10,3,6,10,7,10,6],
[1197993,5,6,7,8,8,10,3,10,3],
[1200847,6,10,10,10,8,10,10,10,7],
[1200952,5,8,7,7,10,10,5,7,1],
[1202812,5,3,3,3,6,10,3,1,1],
[1205579,8,7,6,4,4,10,5,1,1],
[1206841,10,5,6,10,6,10,7,7,10],
[1210963,10,10,10,8,6,8,7,10,1],
[1214966,9,7,7,5,5,10,7,8,3],
[1218105,5,10,10,9,6,10,7,10,5],
[1219525,8,10,10,10,5,10,8,10,6],
[1221863,10,10,10,10,7,10,7,10,4],
[1222936,8,7,8,7,5,5,5,10,2],
[1225799,10,6,4,3,10,10,9,10,1],
[1226612,7,5,6,3,3,8,7,4,1],
[1227481,10,5,7,4,4,10,8,9,1],
[1230175,10,10,10,3,10,10,9,10,1],
[1231387,6,8,7,5,6,8,8,9,2],
[1241559,10,8,8,2,8,10,4,8,10],
[1242364,8,10,10,8,6,9,3,10,10],
[144888,8,10,10,8,5,10,7,8,1],
[191250,10,4,4,10,2,10,5,3,3],
[1116116,9,10,10,1,10,8,3,3,1],
[255644,10,5,8,10,3,10,5,1,3],
[274137,8,8,9,4,5,10,7,8,1],
[314428,7,9,4,10,10,3,5,3,3],
[320675,3,3,5,2,3,10,7,1,1],
[390840,8,4,7,1,3,10,3,9,2],
[428903,7,2,4,1,3,4,3,3,1],
[488173,1,4,3,10,4,10,5,6,1],
[508234,7,4,5,10,2,10,3,8,2],
[529329,10,10,10,10,10,10,4,10,10],
[555977,5,6,6,8,6,10,4,10,4],
[606722,5,5,7,8,6,10,7,4,1],
[635844,8,4,10,5,4,4,7,10,1],
[653777,8,3,4,9,3,10,3,3,1],
[667204,7,8,7,6,4,3,8,8,4],
[706426,5,5,5,2,5,10,4,3,1],
[730881,7,6,3,2,5,10,7,4,6],
[752904,10,1,1,1,2,10,5,4,1],
[760239,10,4,6,4,5,10,7,1,1],
[785208,5,4,6,6,4,10,4,3,1],
[797327,6,5,5,8,4,10,3,4,1],
[809912,10,3,3,1,2,10,7,6,1],
[832226,3,4,4,10,5,1,3,3,1],
[850831,2,7,10,10,7,10,4,9,4],
[859350,8,10,10,7,10,10,7,3,8],
[873549,10,3,5,4,3,7,3,5,3],
[877943,3,10,3,10,6,10,5,1,4],
[160296,5,8,8,10,5,10,8,10,3],
[1080233,7,6,6,3,2,10,7,1,1],
[1211265,3,10,8,7,6,9,9,3,8],
[1238948,8,5,6,2,3,10,6,6,1],
[1257200,10,10,10,7,10,10,8,2,1],
[1266154,8,7,8,2,4,2,5,10,1],
[1295186,10,10,10,1,6,1,2,8,1],
[1193544,5,7,9,8,6,10,8,10,1],
[1246562,10,2,2,1,2,6,1,1,2],
[1259008,8,8,9,6,6,3,10,10,1],
[1298416,10,6,6,2,4,10,9,7,1],
[1299161,4,8,7,10,4,10,7,5,1],
[474162,8,7,8,5,5,10,9,10,1],
[1076352,3,6,4,10,3,3,3,4,1],
[1119189,5,8,9,4,3,10,7,1,1],
[1286943,8,10,10,10,7,5,4,8,7],
[1313325,4,10,4,7,3,10,9,10,1],
[412300,10,4,5,4,3,5,7,3,1]
]

print('##a: 4')
print('##id: bhan48')

# %%
# Question #1
print('##1:')

positive_count = len([x for x in labels if x == 4])
negative_count = len([x for x in labels if x == 2])

print('%d,%d' % (positive_count, negative_count))

# %%
# Question #2
print('##2:')

def calc_entropy(labels):
    N = len(labels)
    entropy = 0

    for val in np.unique(np.array(labels)):
        p = len([x for x in labels if x == val]) / N
        entropy = entropy - p * math.log2(p)

    return entropy

entropy = calc_entropy(labels)
print(entropy)

# %%
# Question #3
print('##3:')

def conditional_entropy(data, labels, thresh):
    N = len(labels)

    above = [labels[i] for i in range(N) if data[i] > thresh]
    below = [labels[i] for i in range(N) if data[i] <= thresh]

    n_above = len(above)
    n_below = len(below)

    return (n_above / N) * calc_entropy(above) + (n_below / N) * calc_entropy(below)

def calc_split(data, labels):

    threshold = 0
    inf_gain = 0

    for t in np.unique(np.array(data)):
        cond_entr = conditional_entropy(data, labels, t)

        test = entropy - cond_entr

        if(test > inf_gain):
            inf_gain = test
            threshold = t
    
    return threshold, inf_gain

threshold, inf_gain = calc_split(feature_data, labels)

pos_above_thresh = len([labels[i] for i in range(len(labels)) if feature_data[i] > threshold and labels[i] == 4])
pos_below_thresh = len([labels[i] for i in range(len(labels)) if feature_data[i] <= threshold and labels[i] == 4])
neg_above_thresh = len([labels[i] for i in range(len(labels)) if feature_data[i] > threshold and labels[i] == 2])
neg_below_thresh = len([labels[i] for i in range(len(labels)) if feature_data[i] <= threshold and labels[i] == 2])

print('%d,%d,%d,%d' % (neg_below_thresh, neg_above_thresh, pos_below_thresh, pos_above_thresh))

# %%
# Question #4
print('##4:')

print(inf_gain)

# %%
# Question #5
print('##5:')

class Tree:
    success = 0
    fail = 0
    threshold = 0
    feature = 0
    val = -1

def recursive_split(index, tree_data, tree_index, labels, depth=20):
    tree = Tree()
    
    if np.all(np.array(labels) == 2):
        print(index * ' ' + 'return 2')
        tree.val = 2
        return tree, index
    if np.all(np.array(labels) == 4):
        print(index * ' ' + 'return 4')
        tree.val = 4
        return tree, index
    
    if len(labels) == 1:
        print(index * ' ' + 'return',  labels[0])
        tree.val = labels[0]
        return tree, index
    if len(labels) == 0:
        print(index * ' ' + 'no action needed')
        return
    
    if(index >= depth):
        mode = st.mode(np.array(labels)).mode
        print(index * ' ' + 'return', mode)
        tree.val = mode
        return tree, index
    
    pick = 0
    data = 0
    threshold = 0
    inf_gain = 0

    for i in range(len(tree_data)):
        d = tree_data[i]
        t, ig = calc_split(d, labels)

        if ig > inf_gain:
            pick = i
            data = d
            threshold = t
            inf_gain = ig

    tree.feature = tree_index[pick]
    tree.threshold = threshold
    
    print(index * ' ' + 'if (x%d <= %d)' % (tree_index[pick], threshold))

    pass_index = [i for i in range(len(data)) if data[i] <= threshold]
    fail_index = [i for i in range(len(data)) if data[i] > threshold]

    pass_data = [ [f[i] for i in pass_index] for f in tree_data]
    pass_labels = [labels[i] for i in pass_index]

    tree.success, d_success = recursive_split(index + 1, pass_data, tree_index, pass_labels, depth)

    print(index * ' ' + 'else')

    fail_data = [ [f[i] for i in fail_index] for f in tree_data]
    fail_labels = [labels[i] for i in fail_index]

    tree.fail, d_fail = recursive_split(index + 1, fail_data, tree_index, fail_labels, depth)

    return tree, max(d_success, d_fail)

tree, depth = recursive_split(0, tree_data, tree_index, labels)

# %%
# Question #6
print('##6:')

print(depth)

# %%
# Question #7
print('##7:')

test = [1002945,5,4,4,5,7,10,3,2,1]

def evaluate(test, tree: Tree):
    test = [''] + test
    while tree.val == -1:
        if test[tree.feature] <= tree.threshold:
            tree = tree.success
        else:
            tree = tree.fail

    return int(tree.val)

for t in test_set:
    print(evaluate(t, tree), end=',')
print()

# %%
# Question #8
print('##8:')

pruned_tree, pruned_depth = recursive_split(0, tree_data, tree_index, labels, 7)

# %%
# Question #9
print('##9:')

for t in test_set:
    print(evaluate(t, pruned_tree), end=',')
print()

